package br.edu.ifpr.foz.ifprstore.repositories;

import br.edu.ifpr.foz.ifprstore.models.Seller;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

public class SellerRepositoryTest {


    @Test
    public void deveExibirUmaListaDeSellers(){

        SellerRepository repository = new SellerRepository();



        List<Seller> sellers = repository.getSellers();

        for (Seller s: sellers) {
            System.out.println(s);
        }

    }

    @Test
    public void deveInserirUmRegistroNaTabelaSeller(){

        SellerRepository repository = new SellerRepository();

        Seller sellerFake = new Seller();
        sellerFake.setName("Frodo");
        sellerFake.setEmail("frodo@valinor.com");
        sellerFake.setBirthDate(LocalDate.of(2024, 8, 5));
        sellerFake.setBaseSalary(10000.0);

        Seller seller = repository.insert(sellerFake);

    }

    @Test
    public void deveAtualizarOSalarioDeUmSellerDeUmDepartamento(){

        SellerRepository repository = new SellerRepository();

        repository.updateSalary(1, 1500.0);


    }

    @Test
    public void deveDeletarUmSeller(){

        SellerRepository repository = new SellerRepository();
        repository.delete(9);

    }

    @Test
    public void deveRetornarUmSellerPeloId(){

        SellerRepository repository = new SellerRepository();
        Seller seller = repository.getById(1);

        System.out.println(seller);
        System.out.println("Departamento: --------");
        System.out.println(seller.getDepartment());

    }

    @Test
    public void deveRetornarUmaListaDeSellersPeloIdDoDepartment() {
        SellerRepository repository = new SellerRepository();
        List<Seller> sellersList = repository.findByDepartment(2);

        for (Seller seller: sellersList) {
            System.out.println(seller);
            System.out.println("Departamento: --------");
            System.out.println(seller.getDepartment());
        }

    }
}
