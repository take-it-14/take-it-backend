package com.takeit.common.utils;

public class AccessValidator {

    public static boolean isMaster(String role) {
        return role.equals("MASTER");
    }
    public static boolean isManager(String role) {
        return role.equals("MANAGER");
    }
    public static boolean isSeller(String role) {
        return role.equals("SELLER");
    }
    public static boolean isCustomer(String role) {
        return role.equals("CUSTOMER");
    }
    public static boolean isRequesterAuthorized(String username, String requesterUsername) {
        return username.equals(requesterUsername);
    }
    public static boolean isSellerApproved(String sellerStatus) {
        return sellerStatus.equals("APPROVED");
    }
}
