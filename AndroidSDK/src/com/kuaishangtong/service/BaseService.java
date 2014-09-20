package com.kuaishangtong.service;

import com.kuaishangtong.client.Client;

public class BaseService {
	private Client client;

    public Client getClient() {
            return client;
    }

    public BaseService setClient(Client client) {
            this.client = client;
            return this;
    }
}
