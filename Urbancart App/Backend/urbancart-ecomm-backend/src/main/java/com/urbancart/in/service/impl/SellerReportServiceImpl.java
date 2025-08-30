package com.urbancart.in.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.urbancart.in.model.Seller;
import com.urbancart.in.model.SellerReport;
import com.urbancart.in.repository.SellerReportRepository;
import com.urbancart.in.service.SellerReportService;

//import java.util.List;
//import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SellerReportServiceImpl implements SellerReportService {

    private final SellerReportRepository sellerReportRepository;


    @Override
    public SellerReport getSellerReport(Seller seller) {
        SellerReport report = sellerReportRepository.findBySellerId(seller.getId());
        if(report == null){
            SellerReport newReport = new SellerReport();
            newReport.setSeller(seller);
            return sellerReportRepository.save(newReport);
        }
        return report;
    }


    @Override
    public SellerReport updateSellerReport(SellerReport sellerReport) {

        return sellerReportRepository.save(sellerReport);
    }

}
