/* tslint:disable:no-unused-variable */
import {TestBed, inject} from "@angular/core/testing";
import {LdapService} from "./ldap.service";

describe('LdapService', () => {
    beforeEach(() => {
        TestBed.configureTestingModule({
            providers: [LdapService]
        });
    });

    it('should ...', inject([LdapService], (service: LdapService) => {
        expect(service).toBeTruthy();
    }));
});
