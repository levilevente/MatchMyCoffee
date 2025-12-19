import {Button, Card} from 'react-bootstrap';
import {FaHeart} from 'react-icons/fa';
import {FaCartShopping} from 'react-icons/fa6';
import {Link} from 'react-router-dom';

import type {ProductSummary} from '../../types/ProductsType.ts';

interface ProductTypeProps {
    data: ProductSummary;
}

function ProductCard(props: ProductTypeProps) {
    const {data} = props;

    return (
        <Card style={{width: '18rem'}}>
            <Card.Body>
                <Link
                    to={`/products/${data.id}`}
                    className="text-decoration-none text-reset"
                    aria-label={`View details for ${data.name}`}
                >
                    <Card.Img variant="top" src={data.imageUrl}/>
                    <Card.Title>{data.name}</Card.Title>
                    <Card.Text>{data.price}</Card.Text>
                    <Card.Text>{data.averageRating}</Card.Text>
                    <Card.Text>{data.reviewCount}</Card.Text>
                </Link>
                <Button variant="none">
                    <FaCartShopping/>
                </Button>
                <Button variant="none">
                    <FaHeart/>
                </Button>
            </Card.Body>
        </Card>
    );
}

export default ProductCard;
