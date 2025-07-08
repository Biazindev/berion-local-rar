import { useAddNfMutation } from "../../services/api";
import { NfDTO } from "../NotaFiscal";

export const handleEmitir = async (nfeData: NfDTO) => {
        const [enviarNf] = useAddNfMutation();
        const payload = {
            vendaId: nfeData.vendaId,  
            emitenteId: nfeData.emitenteId
        };

        try {
            await enviarNf(payload).unwrap();
        } catch (e) {
            console.error('Erro ao emitir nota fiscal:', e);
        }
    };
