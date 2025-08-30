package com.urbancart.in.service;


import java.util.List;

import com.urbancart.in.model.Home;
import com.urbancart.in.model.HomeCategory;

public interface HomeService {

    Home creatHomePageData(List<HomeCategory> categories);

}
