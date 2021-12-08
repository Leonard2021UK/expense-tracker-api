package com.codercampus.api.payload.response.responsedto;

import com.codercampus.api.model.*;

import java.util.List;

public class AppInitResponse {
    private final List<ExpenseTracker> expenseTrackers;
    private final List<ExpenseType> expenseTypes;
    private final List<ExpenseAddress> expenseAddresses;
    private final List<ExpensePaymentType> expensePaymentTypes;
    private final List<UnitType> unitTypes;

    public AppInitResponse(AppInitResponseBuilder builder ) {
        this.expenseTrackers = builder.expenseTrackers;
        this.expenseTypes = builder.expenseTypes;
        this.expenseAddresses = builder.expenseAddresses;
        this.expensePaymentTypes = builder.expensePaymentTypes;
        this.unitTypes = builder.unitTypes;
    }

    public List<ExpenseTracker> getExpenseTrackers() {
        return expenseTrackers;
    }

    public List<ExpenseType> getExpenseTypes() {
        return expenseTypes;
    }

    public List<ExpenseAddress> getExpenseAddresses() {
        return expenseAddresses;
    }

    public List<ExpensePaymentType> getExpensePaymentTypes() {
        return expensePaymentTypes;
    }

    public List<UnitType> getUnitTypes() {
        return unitTypes;
    }

    public static class AppInitResponseBuilder{
        private final List<ExpenseTracker> expenseTrackers;
        private List<ExpenseType> expenseTypes;
        private List<ExpenseAddress> expenseAddresses;
        private List<ExpensePaymentType> expensePaymentTypes;
        private List<UnitType> unitTypes;

        public AppInitResponseBuilder (List<ExpenseTracker> expenseTrackers){
            this.expenseTrackers = expenseTrackers;
        }

        public AppInitResponseBuilder expenseTypes(List<ExpenseType> expenseTypes){
            this.expenseTypes = expenseTypes;
            return this;
        }

        public AppInitResponseBuilder expenseAddresses(List<ExpenseAddress> expenseAddresses){
            this.expenseAddresses = expenseAddresses;
            return this;
        }

        public AppInitResponseBuilder expensePaymentTypes(List<ExpensePaymentType> expensePaymentTypes){
            this.expensePaymentTypes = expensePaymentTypes;
            return this;
        }

        public AppInitResponseBuilder unitTypes(List<UnitType> unitTypes){
            this.unitTypes = unitTypes;
            return this;
        }

        public AppInitResponse build(){
            // validateAppInitResponse(appInitResponse);
            // return appInitResponse;
            return  new AppInitResponse(this);
        }

    }
}
