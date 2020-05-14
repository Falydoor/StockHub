import {Component, OnInit} from '@angular/core';
import {IStock} from 'app/shared/model/stock.model';
import {HttpClient, HttpParams, HttpResponse} from '@angular/common/http';
import {SERVER_API_URL} from 'app/app.constants';
import {StockService} from 'app/entities/stock/stock.service';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'jhi-home',
  templateUrl: './dashboard-by-ticker.component.html'
})
export class DashboardByTickerComponent implements OnInit {
  dashboard?: any;
  ticker?: string;
  stocks?: IStock[];
  count: number;

  constructor(private http: HttpClient, private stockService: StockService, private route: ActivatedRoute) {
    this.count = 1;
  }

  ngOnInit(): void {
    this.stockService.query().subscribe((res: HttpResponse<IStock[]>) => {
      this.stocks = res.body || [];
      this.route.paramMap.subscribe(params => {
        this.ticker = params.get('ticker') || 'T';
        this.search();
      });
    });
  }

  search(): void {
    if (this.ticker) {
      const params = new HttpParams().set('ticker', this.ticker);
      this.http.get(`${SERVER_API_URL}api/dashboard/byTicker`, {params, observe: 'response'})
          .subscribe((res: HttpResponse<any>) => (this.dashboard = res.body || []));
    }
  }
}
