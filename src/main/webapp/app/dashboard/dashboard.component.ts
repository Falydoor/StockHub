import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from 'rxjs';

import {LoginModalService} from 'app/core/login/login-modal.service';
import {AccountService} from 'app/core/auth/account.service';
import {Account} from 'app/core/user/account.model';
import {IStock} from 'app/shared/model/stock.model';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {SERVER_API_URL} from 'app/app.constants';
import {StockService} from 'app/entities/stock/stock.service';

@Component({
  selector: 'jhi-home',
  templateUrl: './dashboard.component.html'
})
export class DashboardComponent implements OnInit, OnDestroy {
  account: Account | null = null;
  authSubscription?: Subscription;
  dashboard?: any;
  ticker?: string;
  stocks?: IStock[];
  count?: number = 1;

  constructor(private accountService: AccountService, private loginModalService: LoginModalService, private http: HttpClient, private stockService: StockService) {
  }

  ngOnInit(): void {
    this.authSubscription = this.accountService.getAuthenticationState().subscribe(account => (this.account = account));
    this.stockService.query().subscribe((res: HttpResponse<IStock[]>) => {
      this.stocks = res.body || [];
      this.ticker = this.stocks[0].ticker;
      this.search();
    });
  }

  isAuthenticated(): boolean {
    return this.accountService.isAuthenticated();
  }

  login(): void {
    this.loginModalService.open();
  }

  ngOnDestroy(): void {
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
  }

  search(): void {
    this.dashboard = this.http.get<IStock>(`${SERVER_API_URL}api/dashboard/${this.ticker}`, {observe: 'response'})
      .subscribe((res: HttpResponse<any>) => (this.dashboard = res.body || []));
  }
}
