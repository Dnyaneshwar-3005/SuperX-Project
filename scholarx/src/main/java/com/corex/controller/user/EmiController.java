package com.corex.controller.user;

import com.corex.service.EmiCalculator;
import com.corex.util.SceneNavigator;

public class EmiController {

    public EmiCalculator.Result calculate(double amount, double ratePercent, double years) {
        return EmiCalculator.calculate(amount, ratePercent, years);
    }

    public void openPage(String page) {
        SceneNavigator.go(page);
    }
}