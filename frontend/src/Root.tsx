import { BrowserRouter, Route, Routes } from 'react-router';

import NavigationBar from './components/NavigationBar.tsx';
import BlogPostPage from './pages/BlogPostPage.tsx';
import BlogPostsPage from './pages/BlogPostsPage.tsx';
import CartPage from './pages/CartPage.tsx';
import FavoritesPage from './pages/FavoritesPage.tsx';
import HomePage from './pages/HomePage.tsx';
import ProductPage from './pages/ProductPage.tsx';

function Root() {
    return (
        <div>
            <BrowserRouter>
                <NavigationBar />
                <Routes>
                    <Route path="/" element={<HomePage />} />
                    <Route path="/product/:productId" element={<ProductPage />} />
                    <Route path="/cart" element={<CartPage />} />
                    <Route path="/favorites" element={<FavoritesPage />} />
                    <Route path="/blog" element={<BlogPostsPage />} />
                    <Route path="/blog/:blogId" element={<BlogPostPage />} />
                </Routes>
            </BrowserRouter>
        </div>
    );
}

export default Root;
