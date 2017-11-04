import {FormControl, ValidationErrors} from "@angular/forms/forms";
import {Observable} from "rxjs/Observable";
import {ValidationRule} from "./validation.rules";

export const validatorFor = <T extends ValidationRule>(rule: T) => (control: FormControl) : ValidationErrors => {
    if (rule.matches(control.value)) {
        return Observable.of(null);
    } else {
        return Observable.of({[rule.code]: true});
    }
};