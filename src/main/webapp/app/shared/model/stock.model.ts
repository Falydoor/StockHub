export interface IStock {
  id?: number;
  ticker?: string;
}

export class Stock implements IStock {
  constructor(public id?: number, public ticker?: string) {}
}
