package ru.nigmatzianov.shoppingListBot.service;

import org.springframework.stereotype.Service;
import ru.nigmatzianov.shoppingListBot.domain.User;
import ru.nigmatzianov.shoppingListBot.dto.IcqUserDto;
import ru.nigmatzianov.shoppingListBot.repo.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getOrCreate(IcqUserDto userDto) {
        User user = userRepository.findByExternalId(userDto.getExternalId());
        if (user == null) {
            user = new User();
            user.setName(userDto.getName());
            user.setLastName(userDto.getLastName());
            user.setNick(userDto.getNick());
            user.setExternalId(userDto.getExternalId());
            user.setChatId(userDto.getChatId());

            userRepository.save(user);

        }

        return user;
    }
}
