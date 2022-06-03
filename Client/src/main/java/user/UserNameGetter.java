package user;

public interface UserNameGetter extends common.StatusMessagePrinter {
    enum ActionCode{GET_USERNAME, GET_NEW_USERNAME_BECAUSE_OLD_IS_ALREADY_REGISTERED, GET_NEW_USER_NAME_BECAUSE_OLD_HAS_INVALID_LENGTH}
    String getUserName(ActionCode ac, User.CheckUserNameSteps checkOldUserNameStep);
}
