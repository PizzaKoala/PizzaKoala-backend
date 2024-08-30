package com.PizzaKoala.Pizza.domain.model;

public class S3DTO {
        private final String url;
        private final String uuid;

        public S3DTO(String url, String uuid) {
            this.url = url;
            this.uuid = uuid;
        }

        public String getUrl() {
            return url;
        }

        public String getUuid() {
            return uuid;
        }

}
