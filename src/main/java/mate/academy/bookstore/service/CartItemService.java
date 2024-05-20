package mate.academy.bookstore.service;

import mate.academy.bookstore.dto.cart.CartItemRequestDto;
import mate.academy.bookstore.dto.cart.CartItemResponseDto;
import mate.academy.bookstore.model.ShoppingCart;

public interface CartItemService {

    CartItemResponseDto save(CartItemRequestDto requestDto);

    CartItemResponseDto update(Long itemId, CartItemRequestDto requestDto);

    void remove(Long itemId, ShoppingCart shoppingCart);

    CartItemResponseDto add(CartItemRequestDto requestDto, ShoppingCart shoppingCart);
}
