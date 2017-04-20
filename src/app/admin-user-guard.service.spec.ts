import {inject, TestBed} from "@angular/core/testing";

import {AdminUserGuardService} from "./admin-user-guard.service";

describe('AdminUserGuardService', () => {
    beforeEach(() => {
        TestBed.configureTestingModule({
            providers: [AdminUserGuardService]
        });
    });

    it('should ...', inject([AdminUserGuardService], (service: AdminUserGuardService) => {
        expect(service).toBeTruthy();
    }));
});
