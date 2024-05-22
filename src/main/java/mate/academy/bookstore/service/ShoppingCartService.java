package mate.academy.bookstore.service;

import mate.academy.bookstore.dto.cart.CartItemRequestDto;
import mate.academy.bookstore.dto.cart.CartItemResponseDto;
import mate.academy.bookstore.dto.cart.QuantityRequestDto;
import mate.academy.bookstore.dto.cart.ShoppingCartDto;
import mate.academy.bookstore.model.ShoppingCart;
import mate.academy.bookstore.model.User;

public interface ShoppingCartService {

    ShoppingCart create(User user);

    ShoppingCartDto getByUser(User user);

    CartItemResponseDto addCartItem(User user, CartItemRequestDto requestItemDto);

    CartItemResponseDto updateQuantity(Long itemId, QuantityRequestDto quantity);

    void deleteCartItem(Long cartItemId);
}
