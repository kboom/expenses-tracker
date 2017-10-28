import {Observable} from "rxjs/Observable";
import {ValidationRule} from "src/app/validators/validation.rules";
import {ValidationErrors} from "@angular/forms/forms";

export const validatorFor = <T extends ValidationRule>(rule: T) => (control) : ValidationErrors => {
    if (rule.matches(control.value)) {
        return Observable.of(null);
    } else {
        return Observable.of({[rule.code]: true});
    }
};

export const atLeastOneTrue = (validationCode) => (control) => {
    if (!control.value.find((e) => e)) {
        return {[validationCode]: true}
    } else {
        return null
    }
};