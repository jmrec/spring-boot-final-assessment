package com.example.coffeeshop.agent.service;

import com.example.coffeeshop.agent.tools.CoffeeTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AgentService {
    private final ChatClient chatClient;

    public AgentService(ChatClient.Builder builder, CoffeeTools tools) {
        this.chatClient = builder.defaultSystem("""
                                                        You are Juan's barista assistant for a coffee shop.
                                                        Always begin your very first reply with:
                                                        "Hi, I'm Juan's barista assistant."
                                                        Use the available tools to look up the menu and manage orders.
                                                        Break complex requests into steps and call tools as needed
                                                        before giving a final answer. When you place an order, report
                                                        the order ID and the total back to the user.
                                                        """)
                                 .defaultTools(tools)
                                 .build();
    }

    public String run(String userRequest) {
        return chatClient.prompt()
                        .user(userRequest)
                        .call()
                        .content();
    }
}