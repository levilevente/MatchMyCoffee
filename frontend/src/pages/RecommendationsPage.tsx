import ChatArea from '../components/recommendation/ChatArea.tsx';
import style from './RecommendationsPage.module.css';

export interface Message {
    from: 'user' | 'agent';
    text: string;
}

function RecommendationsPage() {
    return (
        <div className={style.container}>
            <ChatArea />
        </div>
    );
}

export default RecommendationsPage;
