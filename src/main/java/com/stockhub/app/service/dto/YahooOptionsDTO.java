package com.stockhub.app.service.dto;

import java.util.List;

public class YahooOptionsDTO {
    private OptionChainDTO optionChain;

    public OptionChainDTO getOptionChain() {
        return optionChain;
    }

    public YahooOptionsDTO optionChain(OptionChainDTO optionChain) {
        this.optionChain = optionChain;
        return this;
    }

    public static class OptionChainDTO {
        private List<ResultDTO> result;

        public List<ResultDTO> getResult() {
            return result;
        }

        public OptionChainDTO result(List<ResultDTO> result) {
            this.result = result;
            return this;
        }

        public static class ResultDTO {
            private long[] expirationDates;

            private QuoteDTO quote;

            private List<OptionDTO> options;

            public long[] getExpirationDates() {
                return expirationDates;
            }

            public ResultDTO expirationDates(long[] expirationDates) {
                this.expirationDates = expirationDates;
                return this;
            }

            public QuoteDTO getQuote() {
                return quote;
            }

            public ResultDTO quote(QuoteDTO quote) {
                this.quote = quote;
                return this;
            }

            public List<OptionDTO> getOptions() {
                return options;
            }

            public ResultDTO options(List<OptionDTO> options) {
                this.options = options;
                return this;
            }

            public static class OptionDTO {
                private List<CallDTO> calls;

                public List<CallDTO> getCalls() {
                    return calls;
                }

                public OptionDTO calls(List<CallDTO> calls) {
                    this.calls = calls;
                    return this;
                }
            }
        }
    }
}
