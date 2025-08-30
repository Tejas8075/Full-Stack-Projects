package com.urbancart.in.service;


import java.util.List;

import com.urbancart.in.model.Order;
import com.urbancart.in.model.Seller;
import com.urbancart.in.model.Transaction;

public interface TransactionService {

    Transaction createTransaction(Order order);
    List<Transaction> getTransactionBySeller(Seller seller);
    List<Transaction>getAllTransactions();
}
