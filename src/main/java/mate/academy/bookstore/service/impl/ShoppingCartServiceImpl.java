package mate.academy.bookstore.service.impl;

import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.cart.CartItemRequestDto;
import mate.academy.bookstore.dto.cart.CartItemResponseDto;
import mate.academy.bookstore.dto.cart.QuantityRequestDto;
import mate.academy.bookstore.dto.cart.ShoppingCartDto;
import mate.academy.bookstore.exception.EntityNotFoundException;
import mate.academy.bookstore.mapper.CartItemMapper;
import mate.academy.bookstore.mapper.ShoppingCartMapper;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.model.CartItem;
import mate.academy.bookstore.model.ShoppingCart;
import mate.academy.bookstore.model.User;
import mate.academy.bookstore.repository.book.BookRepository;
import mate.academy.bookstore.repository.cart.CartItemRepository;
import mate.academy.bookstore.repository.cart.ShoppingCartRepository;
import mate.academy.bookstore.service.ShoppingCartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository cartRepository;
    private final ShoppingCartMapper cartMapper;
    private final CartItemRepository itemRepository;
    private final CartItemMapper itemMapper;
    private final BookRepository bookRepository;

    @Override
    @Transactional
    public ShoppingCart create(User user) {
        return cartRepository.save(new ShoppingCart(user));
    }

    @Override
    @Transactional
    public ShoppingCartDto getByUser(User user) {
        ShoppingCart cart = getCart(user);
        return cartMapper.toDto(cart);
    }

    @Override
    @Transactional
    public CartItemResponseDto addCartItem(User user, CartItemRequestDto requestItemDto) {
        ShoppingCart cart = getCart(user);
        Book book = getBook(requestItemDto.getBookId());

        CartItem cartItem = itemRepository.findByShoppingCartAndBook(cart, book)
                                          .orElseGet(() -> {
                                              CartItem item = new CartItem();
                                              item.setShoppingCart(cart);
                                              return item;
                                          });
        cartItem.setBook(book);
        cartItem.setQuantity(requestItemDto.getQuantity());
        return itemMapper.toDto(itemRepository.save(cartItem));
    }

    @Override
    @Transactional
    public CartItemResponseDto updateQuantity(Long itemId, QuantityRequestDto quantity) {
        CartItem cartItem = getCartItem(itemId);

        cartItem.setQuantity(quantity.getQuantity());
        return itemMapper.toDto(itemRepository.save(cartItem));
    }

    @Override
    @Transactional
    public void deleteCartItem(Long itemId) {
        CartItem cartItem = getCartItem(itemId);

        ShoppingCart shoppingCart = cartItem.getShoppingCart();
        shoppingCart.getCartItems().remove(cartItem);
        itemRepository.delete(cartItem);
    }

    private ShoppingCart getCart(User user) {
        return cartRepository.findByUser(user)
                             .orElseGet(() -> cartRepository.save(new ShoppingCart(user)));

    }

    private Book getBook(Long bookId) {
        return bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException(
                "Unable to proceed: Book not found with id: " + bookId));
    }

    private CartItem getCartItem(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(() -> new EntityNotFoundException(
                "Unable to proceed: Cart item not found with id: " + itemId));
    }
}
