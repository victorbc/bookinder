package equipe.projetoes.models;

/**
 * Created by Victor on 05-Sep-16.
 */
public class Account {
    private String login;
    private String pass;
    private String email;
    private boolean firstTime;
    private Long id;

    public Account() {
        login = "";
        pass = "";
        email = "";
        firstTime = true;
    }

    public Account(String login, String pass, String email, boolean firstTime) {
        this.login = login;
        this.pass = pass;
        this.email = email;
        this.firstTime = firstTime;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isFirstTime() {
        return firstTime;
    }

    public void setFirstTime(boolean firstTime) {
        this.firstTime = firstTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
