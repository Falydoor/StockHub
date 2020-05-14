import {Route} from '@angular/router';
import {Authority} from 'app/shared/constants/authority.constants';
import {DashboardByTickerComponent} from 'app/dashboard/dashboard-by-ticker.component';
import {DashboardByExpirationComponent} from 'app/dashboard/dashboard-by-expiration.component';

export const DASHBOARD_ROUTES: Route[] = [
  {
    path: 'dashboardByTicker',
    component: DashboardByTickerComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Dashboard By Ticker'
    }
  },
  {
    path: 'dashboardByTicker/:ticker',
    component: DashboardByTickerComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Dashboard By Ticker'
    }
  },
  {
    path: 'dashboardByExpiration',
    component: DashboardByExpirationComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Dashboard By Expiration'
    }
  }
];
