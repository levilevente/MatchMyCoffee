import {Card} from 'react-bootstrap';
import {Link} from 'react-router-dom';

import type {ProductSummary} from '../../types/ProductsType.ts';
import AddToCartButton from '../common/AddToCartButton.tsx';
import AddToFavoritesButton from '../common/AddToFavoritesButton.tsx';
import StarRating from '../common/StarRating.tsx';
import style from './ProductCard.module.css';

interface ProductTypeProps {
    data: ProductSummary;
    inCart?: boolean;
}

function ProductCard(props: ProductTypeProps) {
    const {data, inCart} = props;

    return (
        <Card style={{width: '18rem'}}>
            <Card.Body>
                <Link
                    to={`/products/${data.id}`}
                    className="text-decoration-none text-reset"
                    aria-label={`View details for ${data.name}`}
                >
                    <Card.Img variant="top" src={data.imageUrl} className={style.image}/>
                    <Card.Title>{data.name}</Card.Title>
                    <Card.Text>{data.price} $</Card.Text>
                    <div>
                        <StarRating rating={data.averageRating} showCount={data.reviewCount}/>
                    </div>
                </Link>
                <AddToCartButton product={data} variant={'icon-only'} inCart={inCart}/>
                <AddToFavoritesButton product={data} variant={'icon-only'}/>
            </Card.Body>
        </Card>
    );
}

export default ProductCard;
