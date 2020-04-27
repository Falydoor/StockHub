import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';

import {StockHubSharedModule} from 'app/shared/shared.module';
import {DASHBOARD_ROUTE} from 'app/dashboard/dashboard.route';
import {DashboardComponent} from 'app/dashboard/dashboard.component';

@NgModule({
  imports: [StockHubSharedModule, RouterModule.forChild([DASHBOARD_ROUTE])],
  declarations: [DashboardComponent]
})
export class StockHubDashboardModule {
}
