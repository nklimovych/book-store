package mate.academy.bookstore.service.impl;

import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.cart.CartItemRequestDto;
import mate.academy.bookstore.dto.cart.CartItemResponseDto;
import mate.academy.bookstore.exception.EntityNotFoundException;
import mate.academy.bookstore.mapper.CartItemMapper;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.model.CartItem;
import mate.academy.bookstore.model.ShoppingCart;
import mate.academy.bookstore.repository.book.BookRepository;
import mate.academy.bookstore.repository.cart.CartItemRepository;
import mate.academy.bookstore.service.CartItemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository itemRepository;
    private final BookRepository bookRepository;
    private final CartItemMapper itemMapper;

    @Override
    public CartItemResponseDto save(CartItemRequestDto requestDto) {
        Book book = getBookOrElseThrow(requestDto.getBookId());

        CartItem cartItem = new CartItem();
        cartItem.setBook(book);
        cartItem.setQuantity(requestDto.getQuantity());

        CartItem savedItem = itemRepository.save(cartItem);
        return itemMapper.toDto(savedItem);
    }

    @Override
    public CartItemResponseDto add(CartItemRequestDto requestDto, ShoppingCart shoppingCart) {
        Book book = getBookOrElseThrow(requestDto.getBookId());

        CartItem cartItem = itemRepository.findByShoppingCartAndBook(shoppingCart, book)
                                          .orElseGet(() -> {
                                              CartItem item = new CartItem();
                                              item.setShoppingCart(shoppingCart);
                                              return item;
                                          });
        cartItem.setBook(book);
        cartItem.setQuantity(requestDto.getQuantity());
        CartItem savedItem = itemRepository.save(cartItem);
        return itemMapper.toDto(savedItem);
    }

    @Override
    public CartItemResponseDto update(Long itemId, CartItemRequestDto requestDto) {
        CartItem cartItem = getCartItemOrElseThrow(itemId);

        Book book = getBookOrElseThrow(requestDto.getBookId());

        cartItem.setBook(book);
        cartItem.setQuantity(requestDto.getQuantity());

        CartItem updatedItem = itemRepository.save(cartItem);
        return itemMapper.toDto(updatedItem);
    }

    @Override
    @Transactional
    public void remove(Long itemId, ShoppingCart shoppingCart) {
        CartItem cartItem = getCartItemOrElseThrow(itemId);

        shoppingCart.getCartItems().remove(cartItem);
        itemRepository.delete(cartItem);
    }

    private Book getBookOrElseThrow(Long bookId) {
        return bookRepository.findById(bookId).orElseThrow(() ->
                new EntityNotFoundException(
                        "Unable to proceed: Book not found with id: " + bookId));
    }

    private CartItem getCartItemOrElseThrow(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(() ->
                new EntityNotFoundException(
                        "Unable to proceed: Cart item not found with id: " + itemId));
    }
}
