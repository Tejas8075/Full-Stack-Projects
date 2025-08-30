package com.urbancart.in.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.urbancart.in.model.Deal;
import com.urbancart.in.model.HomeCategory;
import com.urbancart.in.repository.DealRepository;
import com.urbancart.in.repository.HomeCategoryRepository;
import com.urbancart.in.service.DealService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DealServiceImpl implements DealService {
	private final DealRepository dealRepository;
	private final HomeCategoryRepository homeCategoryRepository;

	@Override
	public Deal createDeal(Deal deal) {
		HomeCategory category = homeCategoryRepository.findById(deal.getCategory().getId()).orElse(null);
		Deal newDeal = new Deal();
		newDeal.setCategory(category);
		newDeal.setDiscount(deal.getDiscount());
		return dealRepository.save(newDeal);
	}
//
//    @Override
//    public List<Deal> createDeals(List<Deal> deals) {
//        if(dealRepository.findAll().isEmpty()){
//            return dealRepository.saveAll(deals);
//        }
//        else return dealRepository.findAll();
//
//    }

	@Override
	public List<Deal> getDeals() {
		return dealRepository.findAll();
	}

	@Override
	public Deal updateDeal(Deal deal, Long id) throws Exception {
		Deal existingDeal = dealRepository.findById(id).orElse(null);
		HomeCategory category = homeCategoryRepository.findById(deal.getCategory().getId()).orElse(null);

		if (existingDeal != null) {
			if (deal.getDiscount() != null) {
				existingDeal.setDiscount(deal.getDiscount());
			}
			if (category != null) {
				existingDeal.setCategory(category);
			}
			return dealRepository.save(existingDeal);
		}
		throw new Exception("Deal not found");
	}

	@Override
	public void deleteDeal(Long id) throws Exception {
		Deal deal = dealRepository.findById(id).orElse(null);

		if (deal != null) {

			dealRepository.delete(deal);
		}

	}

}
