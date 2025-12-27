import style from './EmptyState.module.css';

interface Props {
    title: string;
    subtitle: string;
}

const EmptyState = ({ title, subtitle }: Props) => {
    return (
        <div className={style.containerEmpty}>
            <h3>{title}</h3>
            <p>{subtitle}</p>
        </div>
    );
};

export default EmptyState;
