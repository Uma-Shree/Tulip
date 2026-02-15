import { useState, useEffect } from 'react';
import axios from 'axios';
import './App.css';

// Configure axios to send cookies for session management
axios.defaults.withCredentials = true;

function App() {
  const [products, setProducts] = useState([]);
  const [cart, setCart] = useState({ items: [], total: 0, totalItems: 0 });
  const [isCartOpen, setIsCartOpen] = useState(false);
  const [categoryFilter, setCategoryFilter] = useState('All');

  // --- AUTH STATES ---
  const [isLoginOpen, setIsLoginOpen] = useState(false);
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [token, setToken] = useState(localStorage.getItem('token'));
  const [currentUser, setCurrentUser] = useState(localStorage.getItem('username'));

  useEffect(() => {
    fetchProducts();
    fetchCart(); // Load cart on mount
  }, []);

  const fetchProducts = async () => {
    try {
      const response = await axios.get('http://localhost:8080/api/products');
      setProducts(response.data);
    } catch (error) {
      console.error("Error fetching products:", error);
    }
  };

  // --- CART API FUNCTIONS ---

  const fetchCart = async () => {
    try {
      const config = token ? {
        headers: { 'Authorization': `Bearer ${token}` }
      } : {};

      const response = await axios.get('http://localhost:8080/api/cart', config);
      setCart(response.data);
    } catch (error) {
      console.error("Error fetching cart:", error);
    }
  };

  const addToCart = async (product) => {
    try {
      const config = token ? {
        headers: { 'Authorization': `Bearer ${token}` }
      } : {};

      const response = await axios.post(
        'http://localhost:8080/api/cart/add',
        {
          productId: product.id,
          quantity: 1
        },
        config
      );

      setCart(response.data);
      setIsCartOpen(true);
    } catch (error) {
      if (error.response?.data) {
        alert(error.response.data);
      } else {
        console.error("Error adding to cart:", error);
      }
    }
  };

  const removeFromCart = async (cartItemId) => {
    try {
      const config = token ? {
        headers: { 'Authorization': `Bearer ${token}` }
      } : {};

      const response = await axios.delete(
        `http://localhost:8080/api/cart/items/${cartItemId}`,
        config
      );

      setCart(response.data);
    } catch (error) {
      console.error("Error removing from cart:", error);
    }
  };

  const updateQuantity = async (cartItemId, newQuantity) => {
    if (newQuantity < 1) {
      removeFromCart(cartItemId);
      return;
    }

    try {
      const config = token ? {
        headers: { 'Authorization': `Bearer ${token}` }
      } : {};

      const response = await axios.put(
        `http://localhost:8080/api/cart/items/${cartItemId}?quantity=${newQuantity}`,
        {},
        config
      );

      setCart(response.data);
    } catch (error) {
      if (error.response?.data) {
        alert(error.response.data);
      } else {
        console.error("Error updating quantity:", error);
      }
    }
  };

  // --- LOGIN FUNCTIONS ---

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post('http://localhost:8080/api/auth/login', {
        username: username,
        password: password
      });

      const receivedToken = response.data.token;

      localStorage.setItem('token', receivedToken);
      localStorage.setItem('username', username);

      setToken(receivedToken);
      setCurrentUser(username);
      setIsLoginOpen(false);

      // Fetch cart again after login (will merge guest cart with user cart)
      await fetchCart();

      alert("Login Successful!");
    } catch (error) {
      alert("Login Failed! Check username/password.");
    }
  };

  const handleLogout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    setToken(null);
    setCurrentUser(null);

    // Fetch cart again for guest session
    fetchCart();
  };

  const filteredProducts = categoryFilter === 'All'
    ? products
    : products.filter(p => p.category && p.category.name === categoryFilter);

  return (
    <div className="app-container">
      {/* HEADER */}
      <header>
        <div className="header-top">
            <h1>🌷 Tulip Garden & Nursery</h1>

            <div className="header-actions">
                {token ? (
                    <div className="user-info">
                        <span>Welcome, <b>{currentUser}</b>!</span>
                        <button className="logout-btn" onClick={handleLogout}>Logout</button>
                    </div>
                ) : (
                    <button className="login-btn" onClick={() => setIsLoginOpen(true)}>🔑 Login</button>
                )}

                <button className="cart-btn" onClick={() => setIsCartOpen(!isCartOpen)}>
                  🛒 Cart ({cart.totalItems || 0})
                </button>
            </div>
        </div>
        <p>Fresh Flowers, Nursery Plants & Vegetables</p>
      </header>

      {/* CATEGORY TABS */}
      <nav className="categories">
        {['All', 'Flowers', 'Nursery Plants', 'Vegetables'].map(cat => (
          <button
            key={cat}
            className={categoryFilter === cat ? 'active' : ''}
            onClick={() => setCategoryFilter(cat)}>
            {cat === 'All' ? 'All Items' : cat}
          </button>
        ))}
      </nav>

      {/* LOGIN MODAL */}
      {isLoginOpen && (
        <div className="modal-overlay">
            <div className="modal-content">
                <h2>Login</h2>
                <form onSubmit={handleLogin}>
                    <input
                        type="text"
                        placeholder="Username"
                        value={username}
                        onChange={e => setUsername(e.target.value)}
                        required
                    />
                    <input
                        type="password"
                        placeholder="Password"
                        value={password}
                        onChange={e => setPassword(e.target.value)}
                        required
                    />
                    <button type="submit" className="confirm-btn">Login</button>
                    <button type="button" className="cancel-btn" onClick={() => setIsLoginOpen(false)}>Cancel</button>
                </form>
            </div>
        </div>
      )}

      {/* MAIN LAYOUT */}
      <div className="main-layout">
        <div className="product-grid">
          {filteredProducts.map((product) => (
            <div key={product.id} className="product-card">
              {product.imageUrl ? (
                <img src={product.imageUrl} alt={product.name} className="product-image" />
              ) : (
                <div className="image-placeholder">🌿</div>
              )}
              <div className="card-details">
                <h3>{product.name}</h3>
                <div className="care-tags">
                   {product.sunlight && <span className="tag">☀️ {product.sunlight}</span>}
                </div>
                <p className="description">{product.description}</p>
                <div className="card-footer">
                  <span className="price">${product.price}</span>
                  <button onClick={() => addToCart(product)}>Add +</button>
                </div>
              </div>
            </div>
          ))}
        </div>

        {/* CART SIDEBAR */}
        {isCartOpen && (
          <div className="cart-sidebar">
            <div className="cart-header">
              <h2>Your Basket</h2>
              <button onClick={() => setIsCartOpen(false)}>❌</button>
            </div>
            {!cart.items || cart.items.length === 0 ? <p>Your cart is empty.</p> : (
              <div className="cart-items">
                {cart.items.map(item => (
                  <div key={item.id} className="cart-item">
                    <div className="item-details">
                      <span className="item-name">{item.productName}</span>
                      <div className="quantity-controls">
                        <button onClick={() => updateQuantity(item.id, item.quantity - 1)}>−</button>
                        <span>{item.quantity}</span>
                        <button onClick={() => updateQuantity(item.id, item.quantity + 1)}>+</button>
                      </div>
                    </div>
                    <div className="item-price">
                      <span>${item.subtotal.toFixed(2)}</span>
                      <button onClick={() => removeFromCart(item.id)} className="remove-btn">🗑️</button>
                    </div>
                  </div>
                ))}
              </div>
            )}
            <div className="cart-footer">
              <h3>Total: ${cart.total ? cart.total.toFixed(2) : '0.00'}</h3>
              <button className="checkout-btn" onClick={() => {
                  if(!token) { alert("Please Login to Checkout!"); setIsLoginOpen(true); }
                  else { alert("Proceeding to payment..."); }
              }}>
                Checkout
              </button>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}

export default App;
