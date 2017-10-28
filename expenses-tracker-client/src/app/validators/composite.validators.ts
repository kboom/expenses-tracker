import {AbstractControl, ValidationErrors} from "@angular/forms";

export const fieldsAreEqual = (firstField, secondField, invalidCode) => (control: AbstractControl) => {
    let firstValue = control.get(firstField).value;
    let secondValue = control.get(secondField).value;
    if (firstValue != secondValue) {
        control.get(secondField).setErrors({[invalidCode]: true})
    } else {
        return null
    }
};