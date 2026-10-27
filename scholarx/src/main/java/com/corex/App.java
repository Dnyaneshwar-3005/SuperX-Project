package com.corex;

import com.corex.config.AppConstants;
import com.corex.util.SceneNavigator;
import com.corex.view.WelcomeView;
import com.corex.view.user.CompareView;
import com.corex.view.user.DashboardView;
import com.corex.view.user.EmiView;
import com.corex.view.user.LoanDetailsView;
import com.corex.view.user.LoansView;
import com.corex.view.user.LoginView;
import com.corex.view.user.MainLayoutView;
import com.corex.view.user.SavedLoansView;
import com.corex.view.user.SignUpView;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle(AppConstants.APP_NAME);
        SceneNavigator.init(stage);

        // Register pages here. One line for each page we build.
        SceneNavigator.register(AppConstants.PAGE_WELCOME, WelcomeView::new);
        SceneNavigator.register(AppConstants.PAGE_LOGIN, LoginView::new);
        SceneNavigator.register(AppConstants.PAGE_SIGNUP, SignUpView::new);

        // Student pages: the frame (sidebar + top bar) wraps each page's content
        SceneNavigator.register(AppConstants.PAGE_DASHBOARD,
                () -> new MainLayoutView(AppConstants.PAGE_DASHBOARD, new DashboardView()));
        SceneNavigator.register(AppConstants.PAGE_LOANS,
                () -> new MainLayoutView(AppConstants.PAGE_LOANS, new LoansView()));
        SceneNavigator.register(AppConstants.PAGE_EMI,
                () -> new MainLayoutView(AppConstants.PAGE_LOANS, new EmiView()));
        SceneNavigator.register(AppConstants.PAGE_COMPARE,
                () -> new MainLayoutView(AppConstants.PAGE_LOANS, new CompareView()));
        SceneNavigator.register(AppConstants.PAGE_SAVED_LOANS,
                () -> new MainLayoutView(AppConstants.PAGE_LOANS, new SavedLoansView()));
        SceneNavigator.register(AppConstants.PAGE_LOAN_DETAILS,
                () -> new MainLayoutView(AppConstants.PAGE_LOANS, new LoanDetailsView()));

        SceneNavigator.go(AppConstants.PAGE_WELCOME);   // first page shown
        stage.show();
    }
}