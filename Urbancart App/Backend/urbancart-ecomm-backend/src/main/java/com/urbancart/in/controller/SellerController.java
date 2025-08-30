package com.urbancart.in.controller;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.urbancart.in.config.JwtProvider;
import com.urbancart.in.domain.AccountStatus;
import com.urbancart.in.domain.USER_ROLE;
import com.urbancart.in.exception.SellerException;
import com.urbancart.in.model.Seller;
import com.urbancart.in.model.SellerReport;
import com.urbancart.in.model.VerificationCode;
import com.urbancart.in.repository.VerificationCodeRepository;
import com.urbancart.in.response.ApiResponse;
import com.urbancart.in.response.AuthResponse;
import com.urbancart.in.service.EmailService;
import com.urbancart.in.service.SellerReportService;
import com.urbancart.in.service.SellerService;
import com.urbancart.in.service.VerificationService;
import com.urbancart.in.service.impl.CustomeUserServiceImplementation;
import com.urbancart.in.utils.OtpUtils;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/sellers")
@RequiredArgsConstructor
public class SellerController {

	private final SellerService sellerService;
	private final SellerReportService sellerReportService;
	private final EmailService emailService;
	private final VerificationCodeRepository verificationCodeRepository;
	private final VerificationService verificationService;
	private final JwtProvider jwtProvider;
	private final CustomeUserServiceImplementation customeUserServiceImplementation;

	@PostMapping("/sent/login-top")
	public ResponseEntity<ApiResponse> sentLoginOtp(@RequestBody VerificationCode req)
			throws MessagingException, SellerException {
		Seller seller = sellerService.getSellerByEmail(req.getEmail());

		String otp = OtpUtils.generateOTP();
		VerificationCode verificationCode = verificationService.createVerificationCode(otp, req.getEmail());

		String subject = "Zosh Bazaar Login Otp";
		String text = "your login otp is - ";
		emailService.sendVerificationOtpEmail(req.getEmail(), verificationCode.getOtp(), subject, text);

		ApiResponse res = new ApiResponse();
		res.setMessage("otp sent");
		return new ResponseEntity<>(res, HttpStatus.CREATED);
	}

	@PostMapping("/verify/login-top")
	public ResponseEntity<AuthResponse> verifyLoginOtp(@RequestBody VerificationCode req)
			throws MessagingException, SellerException {
//        Seller savedSeller = sellerService.createSeller(seller);

		String otp = req.getOtp();
		String email = req.getEmail();
		VerificationCode verificationCode = verificationCodeRepository.findByEmail(email);

		if (verificationCode == null || !verificationCode.getOtp().equals(otp)) {
			throw new SellerException("wrong otp...");
		}

		Authentication authentication = authenticate(req.getEmail());
		SecurityContextHolder.getContext().setAuthentication(authentication);

		String token = jwtProvider.generateToken(authentication);
		AuthResponse authResponse = new AuthResponse();

		authResponse.setMessage("Login Success");
		authResponse.setJwt(token);
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

		String roleName = authorities.isEmpty() ? null : authorities.iterator().next().getAuthority();

		authResponse.setRole(USER_ROLE.valueOf(roleName));

		return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.OK);
	}

	private Authentication authenticate(String username) {
		UserDetails userDetails = customeUserServiceImplementation.loadUserByUsername("seller_" + username);

		System.out.println("sign in userDetails - " + userDetails);

		if (userDetails == null) {
			System.out.println("sign in userDetails - null " + userDetails);
			throw new BadCredentialsException("Invalid username or password");
		}

		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}

	@PatchMapping("/verify/{otp}")
	public ResponseEntity<Seller> verifySellerEmail(@PathVariable String otp) throws SellerException {

		VerificationCode verificationCode = verificationCodeRepository.findByOtp(otp);

		if (verificationCode == null || !verificationCode.getOtp().equals(otp)) {
			throw new SellerException("wrong otp...");
		}

		Seller seller = sellerService.verifyEmail(verificationCode.getEmail(), otp);

		return new ResponseEntity<>(seller, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<Seller> createSeller(@RequestBody Seller seller) throws SellerException, MessagingException {
		Seller savedSeller = sellerService.createSeller(seller);

		String otp = OtpUtils.generateOTP();
		VerificationCode verificationCode = verificationService.createVerificationCode(otp, seller.getEmail());

		String subject = "Zosh Bazaar Email Verification Code";
		String text = "Welcome to Zosh Bazaar, verify your account using this link ";
		String frontend_url = "http://localhost:3000/verify-seller/";
		emailService.sendVerificationOtpEmail(seller.getEmail(), verificationCode.getOtp(), subject,
				text + frontend_url);
		return new ResponseEntity<>(savedSeller, HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Seller> getSellerById(@PathVariable Long id) throws SellerException {
		Seller seller = sellerService.getSellerById(id);
		return new ResponseEntity<>(seller, HttpStatus.OK);
	}

//	@GetMapping("/profile")
//	public ResponseEntity<Seller> getSellerByJwt(@RequestHeader("Authorization") String jwt) throws SellerException {
//		String email = jwtProvider.getEmailFromJwtToken(jwt);
//		Seller seller = sellerService.getSellerByEmail(email);
//		return new ResponseEntity<>(seller, HttpStatus.OK);
//	}
	
	@GetMapping("/profile")
	public ResponseEntity<Seller> getSellerProfile(Authentication authentication) throws SellerException {
	    String email = authentication.getName(); // because you stored email as principal in JwtTokenValidator
	    Seller seller = sellerService.getSellerByEmail(email);
	    return ResponseEntity.ok(seller);
	}

	@GetMapping("/report")
	public ResponseEntity<SellerReport> getSellerReport(@RequestHeader("Authorization") String jwt)
			throws SellerException {
		String email = jwtProvider.getEmailFromJwtToken(jwt);
		Seller seller = sellerService.getSellerByEmail(email);
		SellerReport report = sellerReportService.getSellerReport(seller);
		return new ResponseEntity<>(report, HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<List<Seller>> getAllSellers(@RequestParam(required = false) AccountStatus status) {
		List<Seller> sellers = sellerService.getAllSellers(status);
		return ResponseEntity.ok(sellers);
	}

	@PatchMapping()
	public ResponseEntity<Seller> updateSeller(@RequestHeader("Authorization") String jwt, @RequestBody Seller seller)
			throws SellerException {

		Seller profile = sellerService.getSellerProfile(jwt);
		Seller updatedSeller = sellerService.updateSeller(profile.getId(), seller);
		return ResponseEntity.ok(updatedSeller);

	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteSeller(@PathVariable Long id) throws SellerException {

		sellerService.deleteSeller(id);
		return ResponseEntity.noContent().build();

	}
}
