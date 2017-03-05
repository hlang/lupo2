/* tslint:disable:no-unused-variable */
import {EqualValidator} from "./equal-validator.directive";

describe('EqualValidatorDirective', () => {
    it('should create an instance', () => {
        const directive = new EqualValidator("password", "false");
        expect(directive).toBeTruthy();
    });
});
