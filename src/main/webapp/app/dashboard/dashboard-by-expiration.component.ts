import {Component, OnInit} from '@angular/core';
import {HttpClient, HttpParams, HttpResponse} from '@angular/common/http';
import {SERVER_API_URL} from 'app/app.constants';
import * as moment from 'moment';

@Component({
  selector: 'jhi-home',
  templateUrl: './dashboard-by-expiration.component.html'
})
export class DashboardByExpirationComponent implements OnInit {
  dashboards?: any;
  expiration?: any;
  expirations?: any;
  count: number;

  constructor(private http: HttpClient) {
    this.count = 1;
  }

  ngOnInit(): void {
    this.http.get(`${SERVER_API_URL}api/dashboard/expirations`, {observe: 'response'})
      .subscribe((res: HttpResponse<any>) => {
        this.expirations = res.body.map((exp: any) => {
          return {
            date: moment.unix(exp.expiration),
            days: exp.expirationDays
          };
        });
        this.expiration = this.expirations[1];
        this.search();
      });
  }

  search(): void {
    this.dashboards = [];
    if (this.expiration) {
      const params = new HttpParams().set('expiration', this.expiration.date.unix());
      this.http.get(`${SERVER_API_URL}api/dashboard/byExpiration`, {params, observe: 'response'})
        .subscribe((res: HttpResponse<any>) => (this.dashboards = res.body || []));
    }
  }
}
