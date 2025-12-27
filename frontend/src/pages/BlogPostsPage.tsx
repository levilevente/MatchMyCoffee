import { useNavigate } from 'react-router';

import MainButton from '../components/common/MainButton.tsx';
import { getBlogPosts } from '../services/main.api.ts';
import type { BlogPostType } from '../types/BlogPostType.ts';
import style from './BlogPostsPage.module.css';

function BlogPostsPage() {
    const blogPosts: BlogPostType[] = getBlogPosts();
    const navigate = useNavigate();

    const handleSeeMore = (id: number) => {
        void navigate(`/blog/${id}`);
    };

    return (
        <div className={style.container}>
            {blogPosts.map(
                (blogPost) =>
                    blogPost.isPublished && (
                        <div key={blogPost.id} style={{ marginBottom: '20px' }}>
                            <hr />
                            <h2>{blogPost.title}</h2>
                            <p>Published At: {new Date(blogPost.publishedAt).toLocaleDateString()}</p>
                            <MainButton
                                text={'See More'}
                                type={'button'}
                                onClick={() => void handleSeeMore(blogPost.id)}
                            />
                            <hr />
                        </div>
                    ),
            )}
        </div>
    );
}

export default BlogPostsPage;
