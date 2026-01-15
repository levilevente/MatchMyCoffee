import { useTranslation } from 'react-i18next';
import { useParams } from 'react-router';

import { useBlogPostById } from '../query/main.query.ts';
import style from './BlogPostPage.module.css';

function BlogPostPage() {
    const { t } = useTranslation();
    const blogId = useParams().blogId;
    const { data: blogPost, isLoading, error } = useBlogPostById(blogId ? parseInt(blogId, 10) : 0);

    if (!blogPost) {
        return <div className={style.container}>Loading...</div>;
    }

    if (isLoading) {
        return <div className={style.container}>Loading...</div>;
    }

    if (error) {
        return <div className={style.container}>Error loading blog post.</div>;
    }

    return (
        <div className={style.container}>
            <div className={style.subHeader}>
                <h1>{blogPost.title}</h1>
                <p>
                    {t('blogPosts.publishedAt')} {new Date(blogPost.publishedAt).toLocaleDateString()}
                </p>
                <p>
                    {t('blogPosts.authorRole')} {blogPost.authorRole}
                </p>
            </div>
            <div dangerouslySetInnerHTML={{ __html: blogPost.content }} className={style.content} />
        </div>
    );
}

export default BlogPostPage;
