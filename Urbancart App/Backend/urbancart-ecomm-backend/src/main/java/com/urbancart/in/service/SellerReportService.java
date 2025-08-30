package com.urbancart.in.service;

//import java.util.List;
//import java.util.Optional;

import com.urbancart.in.model.Seller;
import com.urbancart.in.model.SellerReport;

public interface SellerReportService {
    SellerReport getSellerReport(Seller seller);
    SellerReport updateSellerReport( SellerReport sellerReport);

}
