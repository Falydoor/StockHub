import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { StockHubSharedModule } from 'app/shared/shared.module';
import { StockHubCoreModule } from 'app/core/core.module';
import { StockHubAppRoutingModule } from './app-routing.module';
import { StockHubHomeModule } from './home/home.module';
import { StockHubEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    StockHubSharedModule,
    StockHubCoreModule,
    StockHubHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    StockHubEntityModule,
    StockHubAppRoutingModule
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [MainComponent]
})
export class StockHubAppModule {}
