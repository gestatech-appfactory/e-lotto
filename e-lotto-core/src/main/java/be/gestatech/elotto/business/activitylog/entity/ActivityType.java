package be.gestatech.elotto.business.activitylog.entity;

public enum ActivityType {

    LOGIN_SUCCESS(ActivityFamily.LOGIN),
    LOGIN_FAILED_PASSWORD(ActivityFamily.LOGIN),
    LOGIN_FAILED_USERNAME(ActivityFamily.LOGIN),
    LOGOUT(ActivityFamily.LOGIN),

    CHANGE_PASSWORD(ActivityFamily.CHANGE_PASSWORD),
    CHANGE_PASSWORD_START_FAILED(ActivityFamily.CHANGE_PASSWORD),
    CHANGE_PASSWORD_START_SUCCESS(ActivityFamily.CHANGE_PASSWORD),
    CHANGE_PASSWORD_ACTIVATIONCODE_FAILED(ActivityFamily.CHANGE_PASSWORD),
    CHANGE_PASSWORD_ACTIVATIONCODE_SUCCESS(ActivityFamily.CHANGE_PASSWORD),

    SELF_SUSPEND(ActivityFamily.BLOCKING),

    REGISTRATION_SUCCESS(ActivityFamily.REGISTRATION),
    REGISTRATION_ACTIVATION_SUCCESS(ActivityFamily.REGISTRATION),
    REGISTRATION_WELCOME_MAIL_JOB(ActivityFamily.JOB),

    SHOPPINGCART_ADD_TICKET(ActivityFamily.SHOPPINGCART),
    SHOPPINGCART_ATTACH_PLAYER(ActivityFamily.SHOPPINGCART),
    SHOPPINGCART_REMOVE_TICKET(ActivityFamily.SHOPPINGCART),
    SHOPPINGCART_ADD_UNTRACKED_SESSION_TICKETS(ActivityFamily.SHOPPINGCART),
    SHOPPINGCART_ADJUST_TICKET(ActivityFamily.SHOPPINGCART),

    JOB_TICKET_CLOSED(ActivityFamily.JOB),
    JOB_TICKET_DECAY(ActivityFamily.JOB),
    JOB_TICKET_CLOSING(ActivityFamily.JOB),

    CHANGE_PROFILE(ActivityFamily.PROFILE),
    CHANGE_LIMITS(ActivityFamily.PROFILE),
    ADJUST_LIMITS(ActivityFamily.PROFILE),

    DRAWING_CRAWLER_SUPER6(ActivityFamily.JOB),
    DRAWING_CRAWLER_SPIEL77(ActivityFamily.JOB),
    DRAWING_CRAWLER_KENO(ActivityFamily.JOB),
    DRAWING_CRAWLER_GERMAN6AUS49(ActivityFamily.JOB),
    DRAWING_CRAWLER_EUROJACKPOT(ActivityFamily.JOB),

    PAYMENT_SUBSCRIPTION_SYNCHRONIZE(ActivityFamily.JOB);

    private ActivityFamily activityFamily;

    ActivityType(ActivityFamily activityFamily) {
        this.activityFamily = activityFamily;
    }

    public ActivityFamily getActivityFamily() {
        return activityFamily;
    }

}
