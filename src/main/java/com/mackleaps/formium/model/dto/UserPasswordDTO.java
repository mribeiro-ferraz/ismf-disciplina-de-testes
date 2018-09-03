package com.mackleaps.formium.model.dto;

import com.mackleaps.formium.annotations.PasswordMatches;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@PasswordMatches
public class UserPasswordDTO {

	@NotNull
	@Size(min = 1)
	private String currentPassword;

	@NotNull
	@Size(min = 1)
	private String newPassword;

	@NotNull
	@Size(min = 1)
	private String newPasswordCheck;

    @java.beans.ConstructorProperties({"currentPassword", "newPassword", "newPasswordCheck"})
    public UserPasswordDTO(String currentPassword, String newPassword, String newPasswordCheck) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.newPasswordCheck = newPasswordCheck;
    }

    public UserPasswordDTO() {
    }

    public String getCurrentPassword() {
        return this.currentPassword;
    }

    public String getNewPassword() {
        return this.newPassword;
    }

    public String getNewPasswordCheck() {
        return this.newPasswordCheck;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public void setNewPasswordCheck(String newPasswordCheck) {
        this.newPasswordCheck = newPasswordCheck;
    }
}
