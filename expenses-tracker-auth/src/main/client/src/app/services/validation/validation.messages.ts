import {Injectable} from "@angular/core";

@Injectable()
export class ValidationMessageProvider {

    getValidatorErrorMessage = (validatorCode: string, validatorValue?: any) => {
        return {
            'min': `Minimum value is ${validatorValue.min}`,
            'max': `Maximum value is ${validatorValue.max}`,
            'required': 'Required',
            'email.regex': 'Invalid email address',
            'username.regex': 'Username can contain only alphanumeric characters',
            'timezone.regex': 'Timezone can contain only alphanumeric characters, spaces and hyphens',
            'timezoneLocation.regex': 'Timezone location can contain only letters, spaces and hyphens',
            'password.regex': 'Invalid password. Password must be at least 6 characters long, and contain a number.',
            'password.equality': 'Passwords have to match',
            'minlength': `Minimum length ${validatorValue.requiredLength}`,
            'firstName.regex': `First name can contain only letters`,
            'lastName.regex': `Last name can contain only letters and hyphens`,
            'role:atLeastOne': 'At least one role must be selected'
        }[validatorCode];
    }

}