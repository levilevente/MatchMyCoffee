import { useTranslation } from 'react-i18next';
import { useNavigate } from 'react-router';

import MainButton from '../components/common/MainButton.tsx';
import { useBlogPosts } from '../query/main.query.ts';
import style from './BlogPostsPage.module.css';

function BlogPostsPage() {
    const { t } = useTranslation();
    const { data: blogPosts, isLoading, error } = useBlogPosts();
    const navigate = useNavigate();

    if (isLoading) {
        return <div className={style.container}>Loading...</div>;
    }

    if (error) {
        return <div className={style.container}>Error loading blog posts.</div>;
    }

    const handleSeeMore = (id: number) => {
        void navigate(`/blog/${id}`);
    };

    return (
        <div className={style.container}>
            {blogPosts?.map(
                (blogPost) =>
                    blogPost.isPublished && (
                        <div key={blogPost.id} style={{ marginBottom: '20px' }}>
                            <hr />
                            <h2>{blogPost.title}</h2>
                            <p>
                                {t('blogPosts.publishedAt')} {new Date(blogPost.publishedAt).toLocaleDateString()}
                            </p>
                            <MainButton
                                text={t('blogPosts.seeMore')}
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
