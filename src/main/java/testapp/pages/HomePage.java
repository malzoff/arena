package testapp.pages;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.validator.StringValidator;

import static testapp.WicketApplication.PASSWORD_PATTERN;
import static testapp.WicketApplication.USERNAME_PATTERN;

public class HomePage extends WebPage {

    private static final long serialVersionUID = 1L;

    public HomePage(final PageParameters parameters) {
        super(parameters);

        add(new FeedbackPanel("feedbackPanel"));

        Form form = new Form<MarkupContainer>("form") {

            private TextField<String> usernameField;
            private PasswordTextField passwordField;

            {
                add(usernameField = new TextField<>("username", Model.of("")));
                usernameField.setRequired(true);
                usernameField.add(new StringValidator(6, 16));
                usernameField.add((IValidator<String>) validatable -> {
                    if (!USERNAME_PATTERN.matcher(validatable.getValue()).find()) {
                        error(getString("invalidUserName"));
                    }
                });
                add(passwordField = new PasswordTextField("password", Model.of("")));
                passwordField.setRequired(true);
                passwordField.setResetPassword(true);
                passwordField.add(new StringValidator(6, 16));
                passwordField.add((IValidator<String>) validatable -> {
                    if (!PASSWORD_PATTERN.matcher(validatable.getValue()).find()) {
                        error(getString("invalidPassword"));
                    }
                });
            }
            @Override
            protected void onSubmit() {
                String username = (String)usernameField.getDefaultModelObject();
                String password = (String)passwordField.getDefaultModelObject();
            }
        };
    }
}
