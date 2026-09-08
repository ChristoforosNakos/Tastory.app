package gr.tastory.aueb.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterDto {

    @NotBlank(message = "Το email είναι υποχρεωτικό")
    @Email(message = "Μη έγκυρη διεύθυνση email")
    private String email;

    @NotBlank(message = "Ο κωδικός είναι υποχρεωτικός")
    @Size(min = 6, message = "Ο κωδικός πρέπει να έχει τουλάχιστον 6 χαρακτήρες")
    private String password;

    @NotBlank(message = "Το όνομα είναι υποχρεωτικό")
    @Size(max = 100, message = "Το όνομα δεν πρέπει να ξεπερνά τους 100 χαρακτήρες")
    private String name;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}