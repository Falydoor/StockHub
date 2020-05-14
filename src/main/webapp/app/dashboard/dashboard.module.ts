import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';

import {StockHubSharedModule} from 'app/shared/shared.module';
import {DASHBOARD_ROUTES} from 'app/dashboard/dashboard.route';
import {DashboardByTickerComponent} from 'app/dashboard/dashboard-by-ticker.component';
import {DashboardByExpirationComponent} from 'app/dashboard/dashboard-by-expiration.component';

@NgModule({
  imports: [StockHubSharedModule, RouterModule.forChild(DASHBOARD_ROUTES)],
  declarations: [DashboardByTickerComponent, DashboardByExpirationComponent]
})
export class StockHubDashboardModule {
}
