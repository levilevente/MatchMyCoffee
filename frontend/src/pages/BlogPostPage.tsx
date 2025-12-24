import { useParams } from 'react-router';

import { getBlogPostById } from '../services/main.api.ts';
import style from './BlogPostPage.module.css';

function BlogPostPage() {
    const blogId = useParams().blogId;
    const blogPost = getBlogPostById(blogId ? parseInt(blogId) : -1);

    return (
        <div className={style.container}>
            <div className={style.subHeader}>
                <h1>{blogPost.title}</h1>
                <p>Published At: {new Date(blogPost.publishedAt).toLocaleDateString()}</p>
                <p>Author Role: {blogPost.authorRole}</p>
            </div>
            <div dangerouslySetInnerHTML={{ __html: blogPost.content }} className={style.content} />
        </div>
    );
}

export default BlogPostPage;
