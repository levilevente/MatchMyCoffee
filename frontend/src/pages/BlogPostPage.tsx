import { useTranslation } from 'react-i18next';
import { useParams } from 'react-router';

import { getBlogPostById } from '../services/main.api.ts';
import style from './BlogPostPage.module.css';

function BlogPostPage() {
    const { t } = useTranslation();
    const blogId = useParams().blogId;
    const blogPost = getBlogPostById(blogId ? parseInt(blogId) : -1);

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
