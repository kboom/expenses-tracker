export class ValidationRule {

    constructor(
        public readonly rule: Function,
        public readonly code: string
    ) {}

    matches = (value) => this.rule(value);

    static ruleOf(rule: any, code: string) {
        return new ValidationRule(rule, code)
    }

}

export const PASSWORD_REGEX = ValidationRule.ruleOf((value) => value.match(/^(?=.*[0-9])[a-zA-Z0-9!@#$%^&*]{6,100}$/), "password.regex");
export const USERNAME_REGEX = ValidationRule.ruleOf((value) => value.match(/^[a-zA-Z0-9]*$/), "username.regex");
export const FIRST_NAME_REGEX = ValidationRule.ruleOf((value) => value.match(/^[a-zA-Z]*$/), "firstName.regex");
export const LAST_NAME_REGEX = ValidationRule.ruleOf((value) => value.match(/^([a-zA-Z]+[a-zA-Z-][a-zA-Z]+)*$/), "lastName.regex");
export const TIMEZONE_NAME_REGEX = ValidationRule.ruleOf((value) => value.match(/^[[a-zA-Z0-9]+[a-zA-Z0-9 -]*[a-zA-Z0-9]+$/), "timezone.regex");
export const TIMEZONE_LOCATION_REGEX = ValidationRule.ruleOf((value) => value.match(/^[[a-zA-Z]+[a-zA-Z -]*[a-zA-Z]+$/), "timezoneLocation.regex");
export const EMAIL_REGEX = ValidationRule.ruleOf((value) => value.match(/[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?/), "email.regex");