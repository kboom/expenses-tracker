import {AbstractControl, ValidationErrors} from "@angular/forms";

export const fieldsAreEqual = (firstField: any, secondField: any, invalidCode: any) => (control: AbstractControl): any => {
    let firstValue = control.get(firstField).value;
    let secondValue = control.get(secondField).value;
    if (firstValue != secondValue) {
        control.get(secondField).setErrors({[invalidCode]: true})
    } else {
        return null
    }
};