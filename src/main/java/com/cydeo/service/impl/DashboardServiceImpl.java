package com.cydeo.service.impl;

import com.cydeo.client.CurrencyExchangeClient;
import com.cydeo.service.DashboardService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Service
public class DashboardServiceImpl implements DashboardService {

    private final CurrencyExchangeClient currencyExchangeClient;

    public DashboardServiceImpl(CurrencyExchangeClient currencyExchangeClient) {
        this.currencyExchangeClient = currencyExchangeClient;
    }


    @Override
    public List<String> getCurrencyExchangeRate() {

        Double Euro= (currencyExchangeClient.getCurrencyExchange().getUsd().getEur());
        Double BritishPound= currencyExchangeClient.getCurrencyExchange().getUsd().getGgp();
        Double CanadianDollar= currencyExchangeClient.getCurrencyExchange().getUsd().getCad();
        Double JapaneseYen = currencyExchangeClient.getCurrencyExchange().getUsd().getJpy();
        Double IndianRupee= currencyExchangeClient.getCurrencyExchange().getUsd().getInr();

        return new ArrayList<>(Arrays.asList(String.format("%.4f",Euro),String.format("%.4f",BritishPound),String.format("%.4f",CanadianDollar)
                ,String.format("%.4f",JapaneseYen),String.format("%.4f",IndianRupee)));
    }
}

