package com.tests.automation.apitesting.pojos;

public class BookingDates {

public BookingDates(){


}
    public BookingDates(String cin, String cout) {
        setCheckin(cin);
        setCheckout(cout);
    }

    public String getCheckin() {
        return checkin;
    }

    public void setCheckin(String checkin) {
        this.checkin = checkin;
    }

    public String getCheckout() {
        return checkout;
    }

    public void setCheckout(String checkout) {
        this.checkout = checkout;
    }

    private String checkin;
    private String checkout;

}
