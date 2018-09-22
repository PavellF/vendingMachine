import { NgModule } from '@angular/core';

import { VendingMachineSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
    imports: [VendingMachineSharedLibsModule],
    declarations: [JhiAlertComponent, JhiAlertErrorComponent],
    exports: [VendingMachineSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class VendingMachineSharedCommonModule {}
